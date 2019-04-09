import sys
from selenium import webdriver
from lxml import etree
from redis import StrictRedis
from bs4 import BeautifulSoup
from selenium.webdriver.chrome.options import Options


def get_detail(browser,url,title_rule,source_rule,text_rule):
	#browser = option()
	browser.get(url)
	title = browser.find_element_by_xpath(title_rule)
	source = browser.find_element_by_xpath(source_rule)
	soup = BeautifulSoup(browser.page_source, "html5lib")
	text = soup.select(text_rule)
	return {
			'title': title.text,
			'origin': source.text,
			'content': text
	}
def save_redis(url,title_list,data):
	redis = StrictRedis(host='localhost', port=6379)
	if redis.hlen(url) == 4:
		print('already saved')
	else:
		redis.hmset(url,{'title':str(data['title']),'text':str(data['content']),'source':str(data['origin']),'list':str(title_list)})
def option():
	opt = webdriver.ChromeOptions()
	opt.set_headless()
	opt.add_argument('--disable-gpu')
	opt.add_argument('log-level=3')
	browser = webdriver.Chrome(options=opt)
	return browser

#参数里为单引号  外面有或者没有引号都可以运行
def run():
	url = sys.argv[1]
	url_rule = sys.argv[2] 
	title_rule = sys.argv[3]
	source_rule = sys.argv[4]
	text_rule = sys.argv[5]
	browser = option()
	browser.get(url)
	lists = browser.find_elements_by_xpath(url_rule)
	title_list = []
	for i in lists:
		print(i.get_attribute("href"))
		title_list.append(i.get_attribute("title"))
	print(title_list)
	data = get_detail(browser,lists[0].get_attribute("href"),title_rule,source_rule,text_rule)
	print(data)
	save_redis(url,title_list,data)
	browser.quit()
if __name__ == '__main__':
	run()