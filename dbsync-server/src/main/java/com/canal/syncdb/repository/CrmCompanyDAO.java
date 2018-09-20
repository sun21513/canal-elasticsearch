package com.canal.syncdb.repository;

import com.canal.syncdb.bean.crm.CrmCompany;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by-- lxhao on 2016/10/31.
 */
public interface CrmCompanyDAO extends ElasticsearchRepository<CrmCompany, String> {

  /*  Page<CrmCompany> findByNicknameOrTagNameLike(String keyword, Pageable pageRequest);*/
}

