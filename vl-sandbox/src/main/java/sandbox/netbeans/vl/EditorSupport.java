package sandbox.netbeans.vl;

/*-
 * #%L
 * Netbeans Visual Library Sandbox
 * %%
 * Copyright (C) 2017 Community Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author skrymets
 */
public class EditorSupport {

    private static final Logger LOG = LoggerFactory.getLogger(EditorSupport.class);

    public static void go(JComponent contentView) {

        if (SwingUtilities.isEventDispatchThread()) {
            openEditorWindow(contentView);
        } else {
            SwingUtilities.invokeLater(() -> {
                openEditorWindow(contentView);
            });
        }
    }

    private static JComponent wrapWithScrollPane(JComponent sceneContent) {
        final int SMALL_INC = 16;
        final int LARGE_ING = 256;

        JScrollPane scrolls = new javax.swing.JScrollPane(sceneContent);
        scrolls.getHorizontalScrollBar().setUnitIncrement(SMALL_INC);
        scrolls.getVerticalScrollBar().setUnitIncrement(SMALL_INC);

        scrolls.getHorizontalScrollBar().setBlockIncrement(LARGE_ING);
        scrolls.getVerticalScrollBar().setBlockIncrement(LARGE_ING);

        return scrolls;
    }

    private static void openEditorWindow(JComponent contentView) throws HeadlessException {

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = screenSize.width - 200;
        int windowHeight = screenSize.height - 200;

        JFrame appWindow = new JFrame();

        appWindow.getContentPane().add(
                wrapWithScrollPane(contentView),
                java.awt.BorderLayout.CENTER
        );

        appWindow.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        appWindow.add(contentView, BorderLayout.CENTER);
        appWindow.setBounds(
                (screenSize.width - windowWidth) / 2,
                (screenSize.height - windowHeight) / 2,
                windowWidth, windowHeight);

        //appWindow.pack();
        appWindow.setVisible(true);
    }

    private static void setupLAF() {
        /* 
         * Set the Nimbus look and feel
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            LOG.error(ex.getMessage());
            return;
        }
    }

}
