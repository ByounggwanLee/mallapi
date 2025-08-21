## VS CODE 한글 꺠지는 문제 해결
프로젝트 디렉토리에 아래 내용을 .vscode/settings.json에 저장
```
{
    "terminal.integrated.env.windows": {
        "JAVA_TOOL_OPTIONS": "-Dfile.encoding=UTF-8 -Dconsole.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8"
    },
    "java.debug.settings.vmArgs": "-Dfile.encoding=UTF-8 -Dconsole.encoding=UTF-8",
    "terminal.integrated.defaultProfile.windows": "Command Prompt",
    "terminal.integrated.profiles.windows": {
        "Command Prompt": {
            "path": "cmd.exe",
            "args": ["/k", "chcp 65001"]
        }
    }
}
```