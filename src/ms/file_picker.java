package ms;


import javax.swing.JFileChooser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author srijan
 */
public class file_picker {
    public String get_path(){
        String path="";
         JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(null);
            if(res==JFileChooser.APPROVE_OPTION)
                path = chooser.getSelectedFile().getAbsolutePath();
            return path;
    }
    
}
