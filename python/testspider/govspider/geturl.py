
def get_url(url,item):
    if item.find('http://') == 0:
        real_url = item
    elif item.find('./') == 0:
        real_url = url + item
    elif item.find('/') == 0:
        real_url = url[0:url.find('.cn/')+3] + item
    elif item.find('../') == 0:
        index = findsec(url,'/')
        real_url = url[0:index] + item[2:]
    print(real_url)
    return real_url 