/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoapplication;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author doge
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    private String id;
    private String name;
    private int checked;
    
    public Task(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.checked= 0;
    }
}
