package com.example.basic_backend.backend.Bussniss_logic.Service;

import com.example.basic_backend.backend.Bussniss_logic.Entity.StandardResp;

/**
 * @程志豪 时间：15:16
 * 描述：{desp}
 **/
public interface DataService {
    StandardResp AddData();
    StandardResp DelData();
    StandardResp EditData();
    StandardResp FindData();
}
