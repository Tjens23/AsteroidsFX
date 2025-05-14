module Scoring {
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.data.jpa;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;

    // Update to match the actual module name defined in Common's module-info.java
    requires Common;
    // Update to match the actual module name defined in Core's module-info.java
    requires Core;

    // Export all necessary packages
    exports dk.sdu.mmmi.cbse.scoring;
    exports dk.sdu.mmmi.cbse.scoring.model;
    exports dk.sdu.mmmi.cbse.scoring.controller;
    exports dk.sdu.mmmi.cbse.scoring.service;
    exports dk.sdu.mmmi.cbse.scoring.repository;

    // Configure runtime access for Spring and Hibernate
    opens dk.sdu.mmmi.cbse.scoring to spring.core, spring.beans, spring.context;
    opens dk.sdu.mmmi.cbse.scoring.model to spring.core, org.hibernate.orm.core;
    opens dk.sdu.mmmi.cbse.scoring.controller to spring.core, spring.web;
    opens dk.sdu.mmmi.cbse.scoring.service to spring.core, spring.beans;
    opens dk.sdu.mmmi.cbse.scoring.repository to spring.core, spring.beans, spring.data.jpa;
}