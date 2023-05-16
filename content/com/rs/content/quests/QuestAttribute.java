package com.rs.content.quests;

import lombok.Getter;

@Getter
public class QuestAttribute {

    public boolean ignoreCondition;
    private String name;
    private boolean canContinue;

    public QuestAttribute(String name, boolean completed) {
        this.name = name;
        this.canContinue = completed;
    }

    public QuestAttribute(String name, boolean completed, boolean ignoreCondition) {
        this.name = name;
        this.canContinue = completed;
        this.ignoreCondition = ignoreCondition;
    }
}
