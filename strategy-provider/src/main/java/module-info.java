module strategy.provider {
    requires spring.beans;
    requires spring.context;
    requires spring.boot.autoconfigure;

    exports com.strategy.daos;
    exports com.strategy.models;
}