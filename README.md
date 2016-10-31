# WaterMonitor

使用系统的辅助权限功能来实现~~监控电话打入~~监听特定QQ联系人的命令，自动解锁屏幕（如果有的话），打开QQ特定的联系人并打开视频功能，主要目的是为了满足自己上班有时候监控下家里的🐶子有没有拆家

详情请见[简书](http://www.jianshu.com/p/d91e2e015718)

## 编译和运行

- 0.Server端手机登录QQ，用于监控，Client端的QQ添加Server端的QQ为好友，并在Server端的QQ面板中修改Client端的QQ账号备注格式为`WaterMonitor:Client端QQ`，例如QQ号码是`123456`，那么备注改成`WaterMonitor:12345`，那么当Client发送命令`1`到Server端，Server将会打开和QQ号码12345的视频电话，支持多个不同的Client

- 1.`import github.hellocsl.smartmonitor.utils.Privacy;`这个没类只是再记录了自己默认的QQ帐号的常量，所以没上传，可以自己新建一个

- 2.解锁脚本需要自己重新配置（如何配置?见**Root和屏幕解锁**），不同手机解锁方式和密码不一样，命名为**MonitorUnlock.txt**，并放到Sd卡根目录，以`quit`行为结尾

## 实现

辅助服务，当在锁屏或者非QQ面板的时候，分别监听`AccessibilityEvent#TYPE_WINDOW_CONTENT_CHANGED`和`AccessibilityEvent#TYPE_NOTIFICATION_STATE_CHANGED`事件可以读取到QQ的新消息，根据目标联系人和命令的解析，就可以作出相应的操作

### Root和屏幕解锁

需要Root权限，因为需要进行屏幕解锁，而使用辅助权限也很难达到目的，主要使用到`adb input`命令来模拟滑动和点击，[详情看这里](http://doc.okbase.net/travellife/archive/113675.html)

不同的手机的屏幕解锁密码或者方式不一样，所以需要自己来写脚本，脚本名字为**MonitorUnlock.txt**并放到Sd卡根目录，以`quit`行为结尾，[参考我的](https://github.com/BCsl/WaterMonitor/blob/master/script/MonitorUnlock.txt)

**了解更多请见[简书](http://www.jianshu.com/p/d91e2e015718)**

## 效果

![动图](http://diycode.b0.upaiyun.com/photo/2016/51eac4f28e9f56db06147ee9e03362e7.gif)

![装置](http://diycode.b0.upaiyun.com/photo/2016/d5e267d159cee2c56979a362a4b9842f.png)

![监听](http://diycode.b0.upaiyun.com/photo/2016/243a7f8d06d9f7b6558fd6c80e21901d.png)
