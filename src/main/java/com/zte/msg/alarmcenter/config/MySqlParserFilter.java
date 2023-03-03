package com.zte.msg.alarmcenter.config;

import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2021/1/21 11:26
 */
@Slf4j
@Configuration
public class MySqlParserFilter implements ISqlParserFilter {

    @Value("${mybatis-plus.global-config.db-config.logic-delete-field}")
    private String logicDeleteField;

    private static final String WHERE = "WHERE";

    private static final String LIMIT = "LIMIT";

    private static final String GROUP_BY = "GROUP BY";

    private static final String AND = "AND";

    private static final String SPACE = " ";

    private static final String ORDER_BY = "ORDER BY";

    @Override
    public boolean doFilter(MetaObject metaObject) {
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (SqlCommandType.SELECT == mappedStatement.getSqlCommandType()) {
            BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
            String sql = boundSql.getSql();
            List<String> tables = new ArrayList<>();
            try {
                Select select = (Select) CCJSqlParserUtil.parse(sql);
                SelectBody selectBody = select.getSelectBody();
                PlainSelect plainSelect = (PlainSelect) selectBody;
                if (!(plainSelect.getFromItem() instanceof Table)) {
                    return false;
                }
                Table table = (Table) plainSelect.getFromItem();
                if (Objects.nonNull(table.getAlias())) {
                    tables.add(table.getAlias().getName());
                } else {
                    tables.add(table.getName());
                }
                if (Objects.nonNull(plainSelect.getJoins()) && !plainSelect.getJoins().isEmpty()) {
                    for (Join join : plainSelect.getJoins()) {
                        Table joinTable = (Table) join.getRightItem();
                        if (joinTable.getAlias() != null) {
                            tables.add(joinTable.getAlias().getName());
                        } else {
                            tables.add(joinTable.getName());
                        }
                    }
                }
            } catch (JSQLParserException e) {
                e.printStackTrace();
            }
            List<String> joins = new ArrayList<>(tables.size());
            for (String table : tables) {
                joins.add(table + "." + logicDeleteField + " = 0");
            }
            String join = StringUtils.join(joins, SPACE + AND + SPACE);
            StringBuilder sqlBuilder = new StringBuilder(sql);
            boolean where = StringUtils.containsIgnoreCase(sql, WHERE);
            boolean limit = StringUtils.containsIgnoreCase(sql, LIMIT);
            boolean group = StringUtils.containsIgnoreCase(sql, GROUP_BY);
            boolean deleted = sql.contains(logicDeleteField);
            boolean orderBy = StringUtils.containsIgnoreCase(sql, ORDER_BY);
            if (deleted) {
                return false;
            }
            if (where) {
                sqlBuilder.insert(StringUtils.indexOfIgnoreCase(sql, WHERE) + 6, SPACE + join + SPACE + AND + SPACE);
            } else {
                if (!limit && !group && !orderBy) {
                    sqlBuilder.append(" ").append(WHERE).append(SPACE).append(join);
                }
                if (group) {
                    sqlBuilder.insert(StringUtils.indexOfIgnoreCase(sql, GROUP_BY), SPACE + WHERE + SPACE + join + SPACE);
                }
                if (!group && orderBy) {
                    sqlBuilder.insert(StringUtils.indexOfIgnoreCase(sql, ORDER_BY) - 1, SPACE + WHERE + SPACE + join + SPACE);
                }
                if (limit && !group && !orderBy) {
                    sqlBuilder.insert(StringUtils.indexOfIgnoreCase(sql, LIMIT), SPACE + WHERE + SPACE + join + SPACE);
                }
            }


            metaObject.setValue(PluginUtils.DELEGATE_BOUNDSQL_SQL, sqlBuilder.toString());
        }
        return false;
    }
}
