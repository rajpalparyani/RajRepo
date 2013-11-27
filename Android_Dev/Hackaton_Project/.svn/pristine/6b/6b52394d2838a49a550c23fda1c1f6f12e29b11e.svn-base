from wax import *

class AboutPanel(VerticalPanel):
    def __init__(self, *args, **kwargs):
        VerticalPanel.__init__(self, *args, **kwargs)

        self.htmlwindow = HTMLWindow(self)
        self.AddComponent(self.htmlwindow, expand="b")
        self.Pack()
        self.Size = (640, 480)

        self.htmlwindow.AppendToPage(HTML)
        #self.htmlwindow.LoadFile("ImageTools.html")


HTML = """\
<h5>Image Tools</h5>
<p>
Image Tools was created by TN70 team.
It has Integrated three tools: <b>Scale</b>, <b>Compare</b>, <b>Cut Image</b>.
</p>

<h5>Documents</h5>
<p>
You can get guide & help documents form <b>\\\\172.16.10.25\\Software\\ImageTools</b>.
</p>

<h5>Feedback</h5>
<p>Any question and problem, please send to below members, thanks</p>
<p>
Ming FangQuan: fqming@telenav.cn<br>
Duan Bo: bduan@telenav.cn<br>
Li Qiang: qli@telenav.cn<br>
</p>

<p>From TN70 Team</p>
"""

