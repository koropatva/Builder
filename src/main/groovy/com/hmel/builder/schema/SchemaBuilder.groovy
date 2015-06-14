package com.hmel.builder.schema;

import groovy.sql.Sql;
import groovy.io.FileType;
import com.hmel.builder.schema.properties.Schema;

class SchemaBuilder {
	
	private def getSqlFile(def email, String sqlFiles) {
		def lst = []
		def dir = new File(sqlFiles)
		dir.eachFileRecurse(FileType.FILES) {file ->
			if(file.name.endsWith(".sql")) {
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

	public def update(Schema schema) {
		def sql = Sql.newInstance(schema.datasource.url, schema.datasource.user, schema.datasource.password, schema.datasource.driver)
		def email = ""
		"git config --local user.email".execute().text.eachLine() { email+= it}
		def sqlComment = schema.sqlComment
		getSqlFile(email, schema.uriSqlFiles).sort{it.name}.each { sqlSyntax ->
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
	}
}
