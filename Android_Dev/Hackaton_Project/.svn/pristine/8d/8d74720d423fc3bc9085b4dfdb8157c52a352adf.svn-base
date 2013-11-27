from wax import *
from screenSize import *
from scaleTool import *
from compareTool import *
from cutTool import *
from helpPage import *


class MainFrame(Frame):
    def Body(self):
        nb = NoteBook(self)
        p1 = ScaleImagePanel(nb)
        p2 = CompareImagePanel(nb)
        p3 = CutImagePanel(nb)
        p4 = AboutPanel(nb)
        size = GetScreenSize()
        p1.SetScreenSize(size)
        p2.SetScreenSize(size)
        p1.SetExitHandler(self.Exit)
        p2.SetExitHandler(self.Exit)
        nb.AddPage(p1, 'Scale Image')
        nb.AddPage(p2, 'Compare Image')
        nb.AddPage(p3, 'Cut Image')
        nb.AddPage(p4, 'About')
        self.AddComponent(nb, expand='both')
        self.Pack()

    def Exit(self, event=None):
        self.Destroy()

app = Application(MainFrame, title='Telenav Image Tools')
app.Run()

