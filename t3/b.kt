package t3

import java.awt.Color
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JTextField
import kotlin.random.Random

fun main() {
    val n = Random.nextInt().toString()
    val s = Socket("localhost", 5000)
    val w = PrintWriter(s.getOutputStream())
    val r = BufferedReader(InputStreamReader(s.getInputStream()))
    val p = JPanel()

    val t = JTextField()
    t.preferredSize = Dimension(100, 25)
    p.add(t)

    val b = JButton("Send")
    b.addActionListener {
        w.println("$n :: " + t.text)
        w.flush()
        t.text = ""
    }
    p.add(b)

    val a = JTextArea().apply {
        preferredSize = Dimension(200, 500)
        isEnabled = false
        lineWrap = true
        disabledTextColor = Color.BLACK
    }
    Thread { while (true) if (r.ready()) {
        val c = r.readLine()
        a.append(c)
        a.append("\n")
        println(c)
    } }.start()
    p.add(a)

    val f = JFrame(n).apply {
        add(p)
        pack()
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setLocationRelativeTo(null)
    }
    f.addWindowListener(object : WindowListener {
        override fun windowClosed(p0: WindowEvent?) { w.close(); s.close() }
        override fun windowOpened(p0: WindowEvent?) {}
        override fun windowClosing(p0: WindowEvent?) {}
        override fun windowIconified(p0: WindowEvent?) {}
        override fun windowDeiconified(p0: WindowEvent?) {}
        override fun windowActivated(p0: WindowEvent?) {}
        override fun windowDeactivated(p0: WindowEvent?) {}
    })
    EventQueue.invokeLater { f.isVisible = true }
}
