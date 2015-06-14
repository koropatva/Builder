package com.hmel.builder;

import org.gradle.api.*;
import com.hmel.builder.schema.SchemaBuilder;
import com.hmel.builder.schema.properties.Schema;
import com.hmel.builder.schema.properties.Datasource;

class BuilderPlugin implements Plugin<Project> {
	def void apply(Project project) {
		Schema schema = new Schema()
		schema.sqlComment = "#"
		schema.uriSqlFiles = "sql-files/"
		Datasource datasource = new Datasource()
		datasource.user = "root"
		datasource.password = ""
		datasource.url = "jdbc:mysql://localhost:3306/"
		datasource.driver = "com.mysql.jdbc.Driver"
		schema.datasource = datasource
		project.task('update') << {
			new SchemaBuilder().update(schema)
		}
		
	}
}
