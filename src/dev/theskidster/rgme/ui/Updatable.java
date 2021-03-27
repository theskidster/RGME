package dev.theskidster.rgme.ui;

import dev.theskidster.rgme.commands.Command;
import dev.theskidster.rgme.utils.Mouse;

/**
 * @author J Hoffman
 * Created: Mar 26, 2021
 */

public interface Updatable {
    
    Command update(Mouse mouse);
    
}