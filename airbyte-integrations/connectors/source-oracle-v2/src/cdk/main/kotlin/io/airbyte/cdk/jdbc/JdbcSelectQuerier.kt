/*
 * Copyright (c) 2024 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.cdk.jdbc

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.airbyte.cdk.discover.Field
import io.airbyte.cdk.read.stream.SelectQuerier
import io.airbyte.cdk.read.stream.SelectQuery
import io.airbyte.commons.jackson.MoreMappers
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micronaut.context.annotation.Secondary
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

private val log = KotlinLogging.logger {}

@Secondary
class JdbcSelectQuerier(
    private val jdbcConnectionFactory: JdbcConnectionFactory,
) : SelectQuerier {

    override fun executeQuery(q: SelectQuery, recordVisitor: (record: ObjectNode) -> Boolean) {
        log.info { "Querying ${q.sql}" }
        jdbcConnectionFactory.get().use { conn: Connection ->
            conn.prepareStatement(q.sql).use { stmt: PreparedStatement ->
                q.bindings.forEachIndexed { idx: Int, binding: SelectQuery.Binding ->
                    val paramIdx: Int = idx + 1
                    log.info { "Setting parameter #$paramIdx to $binding." }
                    binding.type.set(stmt, paramIdx, binding.value)
                }
                stmt.executeQuery().use { rs: ResultSet ->
                    while (rs.next()) {
                        val record: ObjectNode = json.objectNode()
                        q.columns.forEachIndexed { idx: Int, column: Field ->
                            val colIdx: Int = idx + 1
                            log.info { "Getting value #$colIdx for $column." }
                            record.set<JsonNode>(column.id, column.type.get(rs, colIdx))
                        }
                        if (recordVisitor(record)) break
                    }
                }
            }
        }
    }

    companion object {
        private val json: JsonNodeFactory = MoreMappers.initMapper().nodeFactory
    }
}
