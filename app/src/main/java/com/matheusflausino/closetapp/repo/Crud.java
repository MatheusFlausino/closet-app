package com.matheusflausino.closetapp.repo;

import java.util.List;

interface Crud
{
	int create(Object item);
	int update(Object item);
	int delete(Object item);
	Object findById(int id);
	List<?> findAll();
}
