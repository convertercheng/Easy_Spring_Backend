import sys
from scrapy.utils.project import get_project_settings
from scrapy.crawler import CrawlerProcess
def run():
	url = sys.argv[1]
	url_rule = sys.argv[2] 
	title_rule = sys.argv[3]
	source_rule = sys.argv[4]
	text_rule = sys.argv[5]
	project_settings = get_project_settings()
	settings = dict(project_settings.copy())
	process = CrawlerProcess(settings)
	process.crawl('universal', **{'url': url,'url_rule': url_rule,'title_rule':title_rule,'source_rule':source_rule,'text_rule':text_rule})
	process.start()
if __name__ =='__main__':
	run()
