package com.agora.data.provider;

public interface IDataProvider {
    IStoreSource getStoreSource();

    IMessageSource getMessageSource();
}
