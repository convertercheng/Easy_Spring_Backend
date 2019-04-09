# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html
import pymongo
from redis import StrictRedis
class GovspiderPipeline(object):
	def open_spider(self,spider):
		self.redis = StrictRedis(host='localhost', port=6379)
	def process_item(self,item,spider):
		self.redis.hmset(item['url'],{'title':str(item['title']),'text':str(item['text']),'source':str(item['source']),'list':str(item['lists'])})
		self.redis.expire(item['url'],60000)

	




  
