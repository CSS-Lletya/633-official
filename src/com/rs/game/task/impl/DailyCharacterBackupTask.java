package com.rs.game.task.impl;

import java.util.Date;

import com.rs.GameConstants;
import com.rs.game.map.World;
import com.rs.game.task.Task;
import com.rs.utilities.Compress;

import io.vavr.control.Try;

/**
 * Creates an automated Character folder daily backup.
 * Very useful for reversing any affected accounts (hacked, rolling back, such..)
 * @author Dennis
 *
 */
public class DailyCharacterBackupTask extends Task {

    public DailyCharacterBackupTask() {
        super(60 * 60 * 24, false);
    }
    
    @Override
    protected void execute() {
    	if (!GameConstants.DAILY_CHARACTER_SAVING)
    		return;
        Date date = new Date();
        Try.run(() -> Compress.zipDirectory("data/characters/", "./data/characters/backups/"+date.getTime()+ ".zip")).onFailure(Throwable::printStackTrace);
    }

    @Override
    public void onCancel() {
        World.get().submit(new DailyCharacterBackupTask());
    }
}
