# -*- coding: utf-8 -*-
import scrapy 
from govspider.geturl import get_url
from govspider.items import GovspiderItem
from bs4 import BeautifulSoup
class UniversalSpider(scrapy.Spider):
    name = 'universal'	
    def __init__(self,url,url_rule,title_rule,source_rule,text_rule):
    	self.url = url
    	self.url_rule = url_rule
    	self.title_rule = title_rule
    	self.source_rule = source_rule
    	self.text_rule = text_rule
    
    def start_requests(self):
        yield scrapy.Request(url=self.url,callback=self.parse_url)
    def parse_url(self,response):
        global lists
        lists = []
        urls = response.xpath(self.url_rule).extract()
        for url in urls:
            real = get_url(self.url,url)
            lists.append(real)
        yield scrapy.Request(url=lists[1],callback=self.parse_detail)
    def parse_detail(self,response):
        item = GovspiderItem()
        item['url'] = response.url
        item['title'] = response.xpath(self.title_rule).extract_first()
        print(item['title'])
        item['source'] = response.xpath(self.source_rule).extract_first()
        print(item['source'])
        soup = BeautifulSoup(response.body,'html5lib')
        item['text'] = str(soup.select(self.text_rule))
        item['lists'] = lists
        yield item


# python run.py http://www.ii.gov.cn/eportal/ui?pageId=303091 //*[@id='96e1b158049b4f66943f8a8bb57b78c5']/div[2]/div/ul/li/a/@href //*[@id='b294e9c1a31f4d7f841f773a64df42f1']/div[2]/div[3]/h1/text() //*[@id='b294e9c1a31f4d7f841f773a64df42f1']/div[2]/div[3]/h2/span[1]/text() .news_nr  