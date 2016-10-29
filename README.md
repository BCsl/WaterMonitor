# WaterMonitor

使用系统的辅助权限功能来实现~~监控电话打入~~监听特定QQ联系人的命令，自动解锁屏幕（如果有的话），打开QQ特定的联系人并打开视频功能，主要目的是为了满足自己上班有时候监控下家里的🐶子有没有拆家，详情请见[简书](http://www.jianshu.com/p/d91e2e015718)

## 使用

首先你得需要两个QQ，还有两台Android📱（作为一个Android开发的，总会有几台更新换代后留下的备用手机，何不充分利用起来，酱紫又可以省下买监控摄像头的💰了）

监控系统分位被监控方（你放在家里作为监控的那台📱）和监控方，作为被监控方，修改监控方QQ联系人的备注格式为**"WaterMonitor:真实的QQ号码"** ，这样当收到监控方的命令，现在待定是`1`，被监控方可以根据这个联系人的备注，获取到需要进行视频的QQ联系人号码，然后发起视频聊天，监控方只需要等待并接收即可

## 实现

辅助服务，当在锁屏或者非QQ面板的时候，分别监听`AccessibilityEvent#TYPE_WINDOW_CONTENT_CHANGED`和`AccessibilityEvent#TYPE_NOTIFICATION_STATE_CHANGED`事件可以读取到QQ的新消息，根据目标联系人和命令的解析，就可以作出相应的操作

### Root和屏幕解锁

需要Root权限，因为需要进行屏幕解锁，而使用辅助权限也很难达到目的，主要使用到【adb input】命令来模拟滑动和点击，[详情看这里](http://doc.okbase.net/travellife/archive/113675.html)

不同的手机的屏幕解锁密码或者方式不一样，所以需要自己来写脚本，脚本名字为**MonitorUnlock.txt**并放到Sd卡根目录，以`quit`为结尾，[参考我的](https://github.com/BCsl/WaterMonitor/blob/master/script/MonitorUnlock.txt)

### 效果

![1](http://upload-images.jianshu.io/upload_images/1097134-754ad45c2dbc4870.gif?imageMogr2/auto-orient/strip)


