package com.github.dbourdette.otto.web.form;

import com.github.dbourdette.otto.service.config.Config;

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
