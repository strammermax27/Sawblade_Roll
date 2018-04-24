package com.dani.game2.tools;

import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.dani.game2.Screens.PlayScreen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by root on 24.12.16.
 */

public class DebugWriter {
    private FileWriter fw;
    private BufferedWriter bw;
    private PlayScreen screen;
    private ArrayMap<String, Integer> graphs;

    private Integer i;

    public DebugWriter(PlayScreen screen) {
        this.screen = screen;
        graphs = new ArrayMap<String, Integer>();
        i = 0;

        try {

            fw = new FileWriter("");///home/daniel/Documents/Programmieren/danipython/Chainsawrun/text.txt");
            bw = new BufferedWriter(fw);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void add_listener(String name){
        graphs.put(name, 0);
    }

    public void update_Value(Integer value, String name){
        int index = graphs.indexOfKey(name);
        graphs.setValue(index,value);

        //System.out.println(graphs.get(name));

    }



    public void write_something(){

        try{
            String content = "NL-";


            for (ObjectMap.Entry<String, Integer> graph : graphs){
                content += graph.key;
                content += "|";
                content += String.valueOf(graph.value);
                content += "-";
            }

            bw.write(content);
            bw.newLine();
            bw.flush();
            fw.flush();



           // //System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        }/*finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }r

        }*/

    }


}
