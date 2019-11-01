package com.imooc.ad.search;

import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.SearchResponse;

/**
 * @Author: hekai
 * @Date: 2019-08-14 16:58
 */
public interface Isearch {
    SearchResponse fetchAds(SearchRequest request);
}
