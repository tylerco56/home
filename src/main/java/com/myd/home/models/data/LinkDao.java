package com.myd.home.models.data;


import com.myd.home.models.Link;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;

@Repository
@Transactional
public interface LinkDao extends CrudRepository<Link, Integer> {

    Link findLinkByUrl(String url);

    Link findLinkBySubject(String subject);

}
