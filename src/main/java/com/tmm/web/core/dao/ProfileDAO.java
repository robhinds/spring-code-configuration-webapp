package com.tmm.web.core.dao;

import java.util.List;

import com.tmm.web.domain.Profile;

public interface ProfileDAO {

	List<Profile> loadAllProfiles();

	List<Profile> loadProfiles(Long[] ids);
}