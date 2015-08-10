package com.hmel.builder.schema;

import java.sql.SQLException;

import org.gradle.api.*;

import groovy.sql.Sql;
import groovy.io.FileType;

import com.hmel.builder.schema.properties.Schema;

class SchemaBuilder {

	private def getSqlFile(def email, String sqlFiles, String sqlSuffix) {
		def lst = []
		def dir = new File(sqlFiles)
		dir.eachFileRecurse(FileType.FILES) {file ->
			if(file.name.endsWith(sqlSuffix)) {
				def flag = false
				file.eachLine() { line ->
					if(line.toString().contains(email))
						flag = true
				}
				if(!flag)
					lst << file
				else
					println "${file.name} was runned before and skipped!"
			}
		}
		return lst
	}

	public def update(Project project) {
		println "INFO: URL - ${project.datasource.url}"
		println "INFO: USER - ${project.datasource.user}"
		println "INFO: PASSWORD - ${project.datasource.password}"
		println "INFO: DRIVER - ${project.datasource.driver}"
		try{
			def sql = Sql.newInstance(project.datasource.url, project.datasource.user, project.datasource.password, project.datasource.driver)
			def email = ""
			"git config --local user.email".execute().text.eachLine() { email+= it}
			def sqlComment = project.schema.sqlComment
			getSqlFile(email, project.schema.uriSqlFiles, project.schema.sqlSuffix).sort{it.name}.each { sqlSyntax ->
				println "Running ${sqlSyntax} script..."
				String query = ""
				sqlSyntax.eachLine() { line ->
					if(line?.trim() && !line.startsWith(sqlComment))
						query += line.trim()
					if(query?.trim() && query.endsWith(";")) {
						println "\t${query}...\n"
						sql.execute(query)
						query = ""
					}
				}
				sqlSyntax.append "\n${sqlComment}${email}"
			}
		}catch(SQLException e){
			println "ERROR: ${e.getMessage()}"
		}
	}
}