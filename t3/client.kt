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
    val name = Random.nextInt().toString()
    val socket = Socket("localhost", 5000)
    val writer = PrintWriter(socket.getOutputStream())
    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
    val panel = JPanel()

    val field = JTextField()
    field.preferredSize = Dimension(100, 25)
    panel.add(field)

    val button = JButton("Send")
    button.addActionListener {
        writer.println("$name :: " + field.text)
        writer.flush()
        field.text = ""
    }
    panel.add(button)

    val text = JTextArea().apply {
        preferredSize = Dimension(200, 500)
        isEnabled = false
        lineWrap = true
        disabledTextColor = Color.BLACK
    }
    Thread { while (true) if (reader.ready()) {
        val line = reader.readLine()
        text.append(line)
        text.append("\n")
        println(line)
    } }.start()
    panel.add(text)

    val frame = JFrame(name).apply {
        add(panel)
        pack()
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setLocationRelativeTo(null)
    }
    frame.addWindowListener(object : WindowListener {
        override fun windowClosed(p0: WindowEvent?) { writer.close(); socket.close() }
        override fun windowOpened(p0: WindowEvent?) {}
        override fun windowClosing(p0: WindowEvent?) {}
        override fun windowIconified(p0: WindowEvent?) {}
        override fun windowDeiconified(p0: WindowEvent?) {}
        override fun windowActivated(p0: WindowEvent?) {}
        override fun windowDeactivated(p0: WindowEvent?) {}
    })
    EventQueue.invokeLater { frame.isVisible = true }
}
