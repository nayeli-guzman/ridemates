package com.ridemates.app.general.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.ridemates.app.general.exception.NotFoundException;
import com.ridemates.app.utils.MethodUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class GenericService<T, RS, I, J extends JpaRepository<T, I>> {
    protected final String modelName;
    protected final J repository;
    protected final ObjectMapper objectMapper;
    protected final ModelMapper modelMapper;

    protected final Class<T> modelType;
    protected final Class<RS> responseType;

    protected GenericService(String modelName,
                             J repository,
                             ObjectMapper objectMapper,
                             ModelMapper modelMapper,
                             Class<T> modelType,
                             Class<RS> responseType) {
        this.modelName = modelName;
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.modelType = modelType;

        this.responseType = responseType;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public List<RS> findAllResponse() {
        return findAll()
                .stream()
                .map(model -> modelMapper.map(model, responseType))
                .toList();
    }

    public T findById(I id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(modelName, id, "id"));
    }

    public RS findByIdResponse(I id) {
        return modelMapper.map(findById(id), responseType);
    }

    public void save(T model) {
        repository.save(model);
    }

    // It doesn't really matter the type of request, the reflection will detect if it's compatible
    public T add(Object request) {
        // TODO: must check if a user is repeated
        // TODO: wdym? - Salva
        T model = modelMapper.map(request, modelType);
        return repository.save(model);
    }

    public RS addResponse(Object request) {
        return modelMapper.map(add(request), responseType);
    }

    public void delete(T model) {
        repository.delete(model);
    }

    public void deleteById(I id) {
        repository.deleteById(id);
    }

    public T update(I id, JsonPatch patch) {
        T patched = MethodUtils.patch(
                findById(id),
                patch,
                objectMapper);
        save(patched);
        return patched;
    }

    public RS updateResponse(I id, JsonPatch patch) {
        T patched = update(id, patch);
        return modelMapper.map(patched, responseType);
    }
}
