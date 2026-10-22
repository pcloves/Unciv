# 教学工作区

课程入口：[从 Unciv 到 Godot：六边形策略游戏系统](unciv-hex-strategy/index.html)。

## 目录约定

- `lessons/NOTES.md`：跨课程的学习背景与教学偏好。
- `lessons/assets/`：所有课程共用的样式、交互组件与页面检查脚本。
- `lessons/<course>/MISSION.md`：该课程的目标与可观察的成功标准。
- `lessons/<course>/COURSE.md`：课程设计、依赖关系与练习验收标准。
- `lessons/<course>/RESOURCES.md`：核对过的一手资料及待补资料。
- `lessons/<course>/NOTES.md`：该课程的进度与待确认事项。
- `lessons/<course>/learning-records/`：已有知识、已证明的理解和纠正过的误解。
- `lessons/<course>/NNNN-<slug>.html`：单课正文，按课程内编号递增。
- `lessons/<course>/reference/`：术语表、算法及源码速查资料。
- `lessons/<course>/verification/`：课程附带的可复跑验证用例，运行方式由对应教材说明。

每个课程子目录是独立的学习工作区，共享文件留在 `lessons/`。统一使用 `lessons` 拼写。

## 后续教学约定

1. 先读共享的 `lessons/NOTES.md`，再读该课程的 MISSION、NOTES、learning-records，了解写作要求和当前学习进度。
2. 先核对源码和资源，再写正文；区分源码摘录、教学简化和迁移建议。
3. 复用 `assets/course.css`、`assets/quiz.js`；HTML 可直接在浏览器打开，无构建步骤。
4. 每课给出明确目标、源码入口、带注释的代码、相关旧课链接和一手阅读来源。
5. 正文结束后先放“心智模型”，然后安排闭卷检索和有反馈的源码练习。
6. 选择题选项以自然、完整、容易比较为准，不为凑成相同字数而删字或造词；反馈解释错误模型，并允许重试。
7. 生成了教材不代表已经掌握；只有学习者的回答、推演或调试证据才能形成掌握记录。
8. 修改学习目标前确认；外部资源注明核对日期和版本适用范围。
9. 面向母语为简体中文的游戏程序员，用自然、准确的中文讲解；注释直接说明行为和原因。具体要求及改写示例见 `lessons/NOTES.md` 的“中文写作要求”。

页面离线可读，外部原文需要联网。源码相对链接用于打开本地文件，固定提交的 GitHub 链接用于查看带行号的版本。

## 页面检查

`assets/check-course.cjs` 使用 Node.js、Playwright 和本机 Microsoft Edge，检查本课程的本地链接、锚点、源码引用、图片、宽屏/移动布局、选择题全部选项反馈、孤立题目控件和打印状态。标注 `data-pair-check` 的代码配对块还会检查左右编号、顺序与行宽，第 15 课已全部纳入；该课另检查桌面代码块内部溢出。Playwright 可安装在独立临时目录，不需要加入游戏工程依赖。

```text
node lessons/assets/check-course.cjs <playwright模块绝对路径> <已有的截图输出目录>
```

第二个参数可省略以跳过截图与 PDF 输出；Playwright 已能直接解析时也可省略第一个参数。游戏源码与本机 Gradle 构建需另行验证，页面检查不覆盖它们。
