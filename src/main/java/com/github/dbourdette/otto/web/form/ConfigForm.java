package com.github.dbourdette.otto.web.form;

import com.github.dbourdette.otto.service.config.Config;

/**
 * Created by IntelliJ IDEA.
 * User: dbourdette
 * Date: 30/10/11
 * Time: 23:50
 * To change this template use File | Settings | File Templates.
 */
public class ConfigForm {
    private String monitoringSource;

    public static ConfigForm read(Config config) {
        ConfigForm form = new ConfigForm();

        form.monitoringSource = config.get(Config.MONITORING_SOURCE);

        return form;
    }

    public String getMonitoringSource() {
        return monitoringSource;
    }

    public void setMonitoringSource(String monitoringSource) {
        this.monitoringSource = monitoringSource;
    }
}
