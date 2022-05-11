# Tidyparse

<!-- Plugin description -->
The main goal of this project is to speed up the process of learning a new language by suggesting ways to fix source code.

Tidyparse expects two files in the same directory -- one ending in `*.tidy` which contains the string to parse (with optional holes) and one ending in `*.cfg` which contains the grammar. If you provide a string containing holes, it will provide some suggestions inside a tool window on the right hand side (can be opened by pressing `Shift` twice in rapid succession and searching for `Tidyparse`. If the string contains no holes, it will print out the parse tree in Chomsky normal form.
<!-- Plugin description end -->

## Getting Started

To use this plugin, first clone the parent repository and initialize the submodule like so:

```bash
git clone https://github.com/breandan/tidyparse && \
cd tidyparse && \
git submodule update --init --recursive && \
./gradlew runIde
```

To launch IntelliJ IDEA with the plugin installed, run: `./gradlew runIde` from the parent directory.

Open a new project, then create a root directory to store the grammar (`*.cfg`) and test cases (`*.tidy`).

To view the parse tree, press `Shift` twice in rapid succession and search for `🔍Tidyparse` to open the tool window.

For example, create the following directory structure:

```
ocaml
├─── ocaml.tidy
└─── ocaml.cfg
```

The file `ocaml.cfg` can contain this grammar:

```
 S -> X
 X -> B | I | F | P
 P -> I O I
 F -> IF | BF
IF -> if B then I else I
BF -> if B then B else B
 O -> + | - | * | /
 I -> 1 | 2 | 3 | 4 | IF
 B -> true | false | B BO B | ( B ) | BF
BO -> and | or
```
The file `ocaml.tidy` can contain this test case:

```
if true then if true then 1 else 2 else 3
```

This should produce the following output (in Chomsky normal form):

```
✅ Current line parses!
I
├── if
└── B.then.I.else.I
    ├── true
    └── then.I.else.I
        ├── then
        └── I.else.I
            ├── I
            │   ├── if
            │   └── B.then.I.else.I
            │       ├── true
            │       └── then.I.else.I
            │           ├── then
            │           └── I.else.I
            │               ├── 1
            │               └── else.I
            │                   ├── else
            │                   └── 2
            └── else.I
                ├── else
                └── 3
```

To view the grammar, test case and parse tree all together, the development environment may be configured as follows:

<img width="1395" alt="Screen Shot 2022-05-10 at 8 16 33 PM" src="https://user-images.githubusercontent.com/175716/167747603-e2bed035-0232-4da7-95fd-f8909fc0eb9a.png">

Tidyparse also accepts holes (`_`) in the test case. Providing such a test case will suggest candidates that are consistent with the provided CFG. 

<img width="1398" alt="Screen Shot 2022-05-10 at 8 34 20 PM" src="https://user-images.githubusercontent.com/175716/167747605-9226f7de-5d92-43b7-bb3b-5300b5320b56.png">
