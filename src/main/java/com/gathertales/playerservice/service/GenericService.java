package com.gathertales.playerservice.service;

public interface GenericService<T> {

    public Iterable<T> findAll();

    public T  find(Long id);

    public T create(T t);

    public void update(T t);

    public void delete(Long id);

}
