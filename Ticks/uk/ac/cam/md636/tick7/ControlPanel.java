package uk.ac.cam.md636.tick7;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.ac.cam.acr31.life.World;
import uk.ac.cam.acr31.life.hash.HashWorld;

public abstract class ControlPanel extends JPanel {

    private JSlider zoomSlider; 
    private JSlider stepSlider;
    private JSlider speedSlider;
    private JRadioButton longButton;
    private JRadioButton arrayButton;
    private JRadioButton agingButton;
    private JRadioButton hashButton;

    protected abstract void onSpeedChange(int value); // Added in tick 7
    protected abstract void onStepChange(int value); // Added in tick 7
    protected abstract void onZoomChange(int value); // Added in tick 7

    private JSlider createNewSlider(int min, int max, int start, String s) {
        Box panel = Box.createHorizontalBox();
        add(panel);
        panel.add(new JLabel(s));
        JSlider slider = new JSlider(min, max, start);
        panel.add(slider);
        return slider;
    }

    private JRadioButton createNewButton(String s, ButtonGroup g, Box b) {
        JRadioButton nrb = new JRadioButton(s, true);
        g.add(nrb);
        b.add(nrb);
        return nrb;
    }

    public ControlPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        zoomSlider = createNewSlider(1, 20, 10, Strings.CONTROL_ZOOM); //Start set to 10
        add(Box.createVerticalStrut(10)); // add 10px of extra space
        
        zoomSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) { //Anonymous class
                if (!zoomSlider.getValueIsAdjusting())
                    onZoomChange(zoomSlider.getValue());
            }
        });        
        
        stepSlider = createNewSlider(0, 10, 0, Strings.CONTROL_STEP);
        add(Box.createVerticalStrut(10)); // add 10px of extra space
        
        stepSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!stepSlider.getValueIsAdjusting())
                    onStepChange(stepSlider.getValue());
            }
        });
        
        speedSlider = createNewSlider(0, 100, 0, Strings.CONTROL_SPEED);
        add(Box.createVerticalStrut(10)); // add 10px of extra space

        speedSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!speedSlider.getValueIsAdjusting())
                    onSpeedChange(speedSlider.getValue());
            }
        });

        Box worldPanel = Box.createHorizontalBox();
        add(worldPanel);
        worldPanel.add(new JLabel(Strings.STORAGE_WORLD_TYPE));
        ButtonGroup group = new ButtonGroup();
        longButton = createNewButton(Strings.STORAGE_LONG, group, worldPanel);
        arrayButton = createNewButton(Strings.STORAGE_ARRAY, group, worldPanel);
        agingButton = createNewButton(Strings.STORAGE_AGING, group, worldPanel);
        hashButton = createNewButton(Strings.STORAGE_HASH, group, worldPanel);
        arrayButton.setSelected(true);
        add(Box.createVerticalStrut(10)); // add 10px of extra space
    }

    public World initialiseWorld(Pattern p) throws PatternFormatException {
        World result = null;
        if (longButton.isSelected()) {
            result = new PackedWorld();
        } else if (arrayButton.isSelected()) {
            result = new ArrayWorld(p.getWidth(), p.getHeight());
        } else if (agingButton.isSelected()) {
            result = new AgingWorld(p.getWidth(), p.getHeight());
        } else if (hashButton.isSelected()) {
            result = new HashWorld(p.getWidth(), p.getHeight());
        }
        if (result != null)
            p.initialise(result);
        return result;
    }
}