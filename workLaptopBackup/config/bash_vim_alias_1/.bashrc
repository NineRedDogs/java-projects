# ~/.bashrc: executed by bash(1) for non-login shells.
# see /usr/share/doc/bash/examples/startup-files (in the package bash-doc)
# for examples

# If not running interactively, don't do anything
[ -z "$PS1" ] && return

# don't put duplicate lines in the history. See bash(1) for more options
# don't overwrite GNU Midnight Commander's setting of `ignorespace'.
export HISTCONTROL=$HISTCONTROL${HISTCONTROL+,}ignoredups
# ... or force ignoredups and ignorespace
export HISTCONTROL=ignoreboth

# append to the history file, don't overwrite it
shopt -s histappend

# for setting history length see HISTSIZE and HISTFILESIZE in bash(1)
# I've plumped for a nice big history file size
export HISTFILESIZE=5000
#
# if i don't want ls/ll commands in the history ....
export HISTIGNORE="&:ls:ll:[bf]g:exit"

# check the window size after each command and, if necessary,
# update the values of LINES and COLUMNS.
shopt -s checkwinsize

# make less more friendly for non-text input files, see lesspipe(1)
[ -x /usr/bin/lesspipe ] && eval "$(SHELL=/bin/sh lesspipe)"

# set variable identifying the chroot you work in (used in the prompt below)
if [ -z "$debian_chroot" ] && [ -r /etc/debian_chroot ]; then
    debian_chroot=$(cat /etc/debian_chroot)
fi

# set a fancy prompt (non-color, unless we know we "want" color)
case "$TERM" in
    xterm-color) color_prompt=yes;;
esac

# uncomment for a colored prompt, if the terminal has the capability; turned
# off by default to not distract the user: the focus in a terminal window
# should be on the output of commands, not on the prompt
#force_color_prompt=yes

if [ -n "$force_color_prompt" ]; then
    if [ -x /usr/bin/tput ] && tput setaf 1 >&/dev/null; then
	# We have color support; assume it's compliant with Ecma-48
	# (ISO/IEC-6429). (Lack of such support is extremely rare, and such
	# a case would tend to support setf rather than setaf.)
	color_prompt=yes
    else
	color_prompt=
    fi
fi

if [ "$color_prompt" = yes ]; then
    PS1='${debian_chroot:+($debian_chroot)}\[\033[01;32m\]\u@\h\[\033[00m\]:\[\033[01;34m\]\w\[\033[00m\]\$ '
else
    PS1='${debian_chroot:+($debian_chroot)}\u@\h:\w\$ '
fi
unset color_prompt force_color_prompt

# If this is an xterm set the title to user@host:dir
case "$TERM" in
xterm*|rxvt*)
    PS1="\[\e]0;${debian_chroot:+($debian_chroot)}\u@\h: \w\a\]$PS1"
    ;;
*)
    ;;
esac

# Alias definitions.
# You may want to put all your additions into a separate file like
# ~/.bash_aliases, instead of adding them here directly.
# See /usr/share/doc/bash-doc/examples in the bash-doc package.

#if [ -f ~/.bash_aliases ]; then
#    . ~/.bash_aliases
#fi

# enable color support of ls and also add handy aliases
if [ -x /usr/bin/dircolors ]; then
    eval "`dircolors -b`"
    alias ls='ls --color=auto'
    #alias dir='dir --color=auto'
    #alias vdir='vdir --color=auto'

    #alias grep='grep --color=auto'
    #alias fgrep='fgrep --color=auto'
    #alias egrep='egrep --color=auto'
fi

# some more ls aliases
alias ll='ls -la'
alias la='ls -A'
alias l='ls -CF'

#
##
# Prompt time, machine and folder in yellow/blue
#     note: the bit between \e]2 and \a sorts out the title of the window
#export PS1="\[\033[1;33;44m\][ \$(date +%H:%M) ][ \u@\h:\w ] $\[\033[0m\] "
export PS1="\[\e]2;\u@\H \w\a\033[1;33;44m\]\w (\$(date +%k:%M)) > \[\033[0m\] "


#export PS1="\[\e]2;\u@\H \w\a\e[32;1m\]>\[\e[0m\] "
#

# sorts out bug 287307 (eclipse)
export GDK_NATIVE_WINDOWS=1 

export EJB3_TUTORIAL_HOME=$HOME/dev/ejb3-tutorial

export JBOSS_HOME=/opt/jboss
export PATH=$PATH:$JBOSS_HOME/bin 

export JAVA_HOME=/usr/lib/jvm/java-7-oracle
export PATH=$PATH:$JAVA_HOME/bin 

export ANT_HOME=/opt/ant
export PATH=$PATH:$ANT_HOME/bin 

export BIN=$HOME/bin
export PATH=$PATH:$BIN

export GRADLE=/usr/local/gradle-1.7
export PATH=$PATH:$GRADLE/bin

export SPRING=/usr/local/spring-tool-suite-3.4.0/sts-3.4.0.M1
export PATH=$PATH:$SPRING
#
#-------------------------------
# set up my aliases
source /home/ag3/.aliases
#-------------------------------
#

 #enable programmable completion features (you don't need to enable
# this, if it's already enabled in /etc/bash.bashrc and /etc/profile
# sources /etc/bash.bashrc).
if [ -f /etc/bash_completion ]; then
    . /etc/bash_completion
fi
