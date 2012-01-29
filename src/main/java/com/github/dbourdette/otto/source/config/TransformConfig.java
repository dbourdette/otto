/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dbourdette.otto.source.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.github.dbourdette.otto.source.Event;
import com.github.dbourdette.otto.source.config.transform.LowerCaseOperation;
import com.github.dbourdette.otto.source.config.transform.NoAccentOperation;
import com.github.dbourdette.otto.source.config.transform.NoPunctuationOperation;
import com.github.dbourdette.otto.source.config.transform.TransformOperation;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author damien bourdette
 */
public class TransformConfig {
    private Map<String, List<TransformOperation>> operations = new HashMap<String, List<TransformOperation>>();

    public static final Map<String, TransformOperation> REGISTRY = new HashMap<String, TransformOperation>();

    static {
        add(new LowerCaseOperation());
        add(new NoAccentOperation());
        add(new NoPunctuationOperation());
    }

    public static void add(TransformOperation operation) {
        REGISTRY.put(operation.getShortName(), operation);
    }

    public static TransformConfig fromDBObject(DBObject dbObject) {
        TransformConfig config = new TransformConfig();

        if (dbObject == null || !dbObject.containsField("operations")) {
            return config;
        }

        List<DBObject> operations = (List<DBObject>) dbObject.get("operations");

        for (DBObject object : operations) {
            String parameter = (String) object.get("name");
            String parameterOperations = (String) object.get("operations");

            config.forParam(parameter).add(parameterOperations);
        }

        return config;
    }

    public class TransformOperationAdder {
        private String parameter;

        private TransformOperationAdder(String parameter) {
            this.parameter = parameter;
        }

        public TransformOperationAdder add(TransformOperation operation) {
            List<TransformOperation> parameterOperations = operations.get(parameter);

            if (parameterOperations == null) {
                parameterOperations = new ArrayList<TransformOperation>();

                operations.put(parameter, parameterOperations);
            }

            parameterOperations.add(operation);

            return this;
        }

        public TransformOperationAdder add(String literal) {
            for (TransformOperation operation : parseOperations(literal)) {
                add(operation);
            }

            return this;
        }

        public TransformOperationAdder replace(String literal) {
            operations.remove(parameter);

            add(literal);

            return this;
        }
    }

    public void applyOn(Event event) {
        for (String parameter : operations.keySet()) {
            Object value = event.get(parameter);

            if (value != null) {
                List<TransformOperation> parameterOperations = operations.get(parameter);

                for (TransformOperation operation : parameterOperations) {
                    value = operation.apply(value);
                }

                event.putValue(parameter, value);
            }
        }
    }

    public TransformOperationAdder forParam(String parameter) {
        return new TransformOperationAdder(parameter);
    }

    public List<TransformOperation> parseOperations(String literal) {
        List<TransformOperation> operations = new ArrayList<TransformOperation>();

        if (StringUtils.isEmpty(literal)) {
            return operations;
        }

        String[] tokens = StringUtils.split(literal, ",");

        for (String token : tokens) {
            TransformOperation operation = REGISTRY.get(StringUtils.trim(token));

            if (operation != null) {
                operations.add(operation);
            }
        }

        return operations;
    }

    public List<Map<String, String>> getConfig() {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        for (String parameter : operations.keySet()) {
            Map<String, String> map = new HashMap<String, String>();

            map.put("name", parameter);
            map.put("operations", getOperationsLiteral(parameter));

            result.add(map);
        }

        return result;
    }

    public DBObject toDBObject() {
        BasicDBObject object = new BasicDBObject("name", "transform");

        List<BasicDBObject> operationsObject = new ArrayList<BasicDBObject>();

        for (String parameter : operations.keySet()) {
            BasicDBObject parameterObject = new BasicDBObject();

            parameterObject.put("name", parameter);
            parameterObject.put("operations", getOperationsLiteral(parameter));

            operationsObject.add(parameterObject);
        }

        object.put("operations", operationsObject);

        return object;
    }

    public String getOperationsLiteral(String parameter) {
        List<String> parameteroperations = new ArrayList<String>();

        for (TransformOperation operation : operations.get(parameter)) {
            parameteroperations.add(operation.getShortName());
        }

        return StringUtils.join(parameteroperations, ",");
    }

    @Override
    public String toString() {
        return "TransformConfig{" +
                "operations=" + operations +
                '}';
    }
}
