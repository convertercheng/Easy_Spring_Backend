package com.qhieco.gateway;

import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletContext;
import java.net.URI;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-10-15 上午9:48
 * <p>
 * 类说明：
 * ${description}
 */
@Slf4j
public class LocationFilter extends ZuulFilter {

    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Autowired
    private ZuulProperties zuulProperties;

    @Autowired
    private RouteLocator routeLocator;

    @Autowired
    private ServletContext servletContext;

    private static final String LOCATION_HEADER = "Location";

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String url = urlPathHelper.getPathWithinApplication(ctx.getRequest());
        Route route = routeLocator.getMatchingRoute(url);
        log.info("contextPath:{}",servletContext.getContextPath());
        log.info("urlPrefix:{}",route.getPrefix());
        if (route != null) {
            Pair<String, String> lh = locationHeader(ctx);
            if (lh != null) {
                String location = lh.second();
                URI originalRequestUri = UriComponentsBuilder
                        .fromHttpRequest(new ServletServerHttpRequest(ctx.getRequest()))
                        .build().toUri();

                UriComponentsBuilder redirectedUriBuilder = UriComponentsBuilder
                        .fromUriString(location);

                UriComponents redirectedUriComps = redirectedUriBuilder.build();

                String newPath = getRestoredPath(this.zuulProperties, route,
                        redirectedUriComps);

                String modifiedLocation = redirectedUriBuilder
                        .scheme(originalRequestUri.getScheme())
                        .host(originalRequestUri.getHost())
                        .port(originalRequestUri.getPort()).replacePath(newPath).build().toString();

                lh.setSecond(modifiedLocation);
            }
        }
        return null;
    }

    private String getRestoredPath(ZuulProperties zuulProperties, Route route,
                                   UriComponents redirectedUriComps) {
        StringBuilder path = new StringBuilder();
        String redirectedPathWithoutGlobal = downstreamHasGlobalPrefix(zuulProperties)
                ? redirectedUriComps.getPath()
                .substring(("/" + zuulProperties.getPrefix()).length())
                : redirectedUriComps.getPath();

        if (downstreamHasGlobalPrefix(zuulProperties)) {
            path.append("/" + zuulProperties.getPrefix());
        }
        else {
            path.append(zuulHasGlobalPrefix(zuulProperties)
                    ? "/" + zuulProperties.getPrefix() : "");
        }

        path.append(downstreamHasRoutePrefix(route) ? servletContext.getContextPath()  : servletContext.getContextPath() + route.getPrefix())
                .append(redirectedPathWithoutGlobal);

        return path.toString();
    }

    private boolean downstreamHasGlobalPrefix(ZuulProperties zuulProperties) {
        return (!zuulProperties.isStripPrefix()
                && StringUtils.hasText(zuulProperties.getPrefix()));
    }

    private boolean zuulHasGlobalPrefix(ZuulProperties zuulProperties) {
        return StringUtils.hasText(zuulProperties.getPrefix());
    }

    private boolean downstreamHasRoutePrefix(Route route) {
        return (!route.isPrefixStripped() && StringUtils.hasText(route.getPrefix()));
    }

    private Pair<String, String> locationHeader(RequestContext ctx) {
        if (ctx.getZuulResponseHeaders() != null) {
            for (Pair<String, String> pair : ctx.getZuulResponseHeaders()) {
                if (pair.first().equals(LOCATION_HEADER)) {
                    return pair;
                }
            }
        }
        return null;
    }


}
