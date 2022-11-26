package io.c0dr.filemanager.controller.amqp.converter;


import io.c0dr.filemanager.model.FileModel;
import io.c0dr.filemanager.model.FileModelBD;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DocumentRegistrationConverter {

    ModelMapper modelMapper = new ModelMapper();

    public FileModelBD toBusiness(FileModel external) {
        return modelMapper.map(external, FileModelBD.class);
    }
}
