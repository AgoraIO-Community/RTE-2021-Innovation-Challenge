<?xml version="1.0"?>
<recipe>
    <merge from="ApiMethods.java.sdw" to="data/service/ApiMethods.kt" />
    <merge from="ApiService.java.sdw" to="data/service/ApiService.kt" />
    <merge from="DataSource.java.sdw" to="data/source/{{dataSource}}.kt" />
    <merge from="LocalDataSource.java.sdw" to="data/source/local/{{localDataSource}}.kt" />
    <merge from="RemoteDataSource.java.sdw" to="data/source/remote/{{remoteDataSource}}.kt" />
    <merge from="Repository.java.sdw" to="data/source/{{repository}}.kt" />
    <instantiate from="Params.java.page.sdw"
      to="data/model/params/{{params}}.kt" />
    <instantiate from="Response.java.page.sdw"
      to="data/model/responses/{{response}}.kt" />
    <instantiate from="DataSource.java.page.sdw"
                 to="data/source/{{dataSource}}.kt" />
    <instantiate from="LocalDataSource.java.page.sdw"
                 to="data/source/local/{{localDataSource}}.kt" />
    <instantiate from="RemoteDataSource.java.page.sdw"
                 to="data/source/remote/{{remoteDataSource}}.kt" />
    <instantiate from="Repository.java.page.sdw"
                 to="data/source/{{repository}}.kt" />
</recipe>
