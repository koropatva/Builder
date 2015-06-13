package com.hmel.builder;

import org.gradle.api.*;
import com.hmel.builder.schema.SchemaBuilder;

class BuilderPlugin implements Plugin<Project> {
	def void apply(Project project) {
		project.task('update') << {
			new SchemaBuilder().update()
		}
	}
}
