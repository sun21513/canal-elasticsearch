package com.canal.syncdb.repository;

import com.canal.syncdb.bean.user.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by-- lxhao on 2016/10/31.
 */
public interface UserDAO extends ElasticsearchRepository<User, String> {

  /*  Page<CrmCompany> findByNicknameOrTagNameLike(String keyword, Pageable pageRequest);*/
}

