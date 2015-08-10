package com.hmel.builder;

import org.gradle.api.*

import com.hmel.builder.schema.SchemaBuilder
import com.hmel.builder.schema.properties.Datasource
import com.hmel.builder.schema.properties.Schema

class BuilderPlugin implements Plugin<Project> {

	def void apply(Project project) {
		project.extensions.create('schema', Schema)
		project.extensions.create('datasource', Datasource)

		project.task('update') << {
			new SchemaBuilder().update(project)
		}
	}
}
