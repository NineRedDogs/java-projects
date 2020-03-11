# ~/.bashrc: executed by bash(1) for non-login shells.
# see /usr/share/doc/bash/examples/startup-files (in the package bash-doc)
# for examples

# Source global definitions
if [ -f /etc/bash.bashrc ]; then
        . /etc/bash.bashrc
fi

# If not running interactively, don't do anything
case $- in
    *i*) ;;
      *) return;;
esac

# don't put duplicate lines or lines starting with space in the history.
# See bash(1) for more options
HISTCONTROL=ignoreboth

# append to the history file, don't overwrite it
shopt -s histappend

# for setting history length see HISTSIZE and HISTFILESIZE in bash(1)
HISTSIZE=1000
HISTFILESIZE=2000

# check the window size after each command and, if necessary,
# update the values of LINES and COLUMNS.
shopt -s checkwinsize

# If set, the pattern "**" used in a pathname expansion context will
# match all files and zero or more directories and subdirectories.
#shopt -s globstar

# make less more friendly for non-text input files, see lesspipe(1)
[ -x /usr/bin/lesspipe ] && eval "$(SHELL=/bin/sh lesspipe)"


# If this is an xterm set the title to user@host:dir
case "$TERM" in
xterm*|rxvt*)
    PS1="\[\e]0;${debian_chroot:+($debian_chroot)}\u@\h: \w\a\]$PS1"
    ;;
*)
    ;;
esac


# Add an "alert" alias for long running commands.  Use like so:
#   sleep 10; alert
alias alert='notify-send --urgency=low -i "$([ $? = 0 ] && echo terminal || echo error)" "$(history|tail -n1|sed -e '\''s/^\s*[0-9]\+\s*//;s/[;&|]\s*alert$//'\'')"'

# Alias definitions.
# You may want to put all your additions into a separate file like
# ~/.bash_aliases, instead of adding them here directly.
# See /usr/share/doc/bash-doc/examples in the bash-doc package.

if [ -f ~/.bash_aliases ]; then
    . ~/.bash_aliases
fi

. /etc/bash_completion

export PATH=$PATH:/sbin:/opt/eclipse:/opt/subversion-1.7.13/subversion:/opt/maven/bin
export PATH=/opt/intelliJ/idea-IC-162.2032.8/bin:$PATH

export CPP_PATH="/usr/include:/usr/include/glib-2.0"

# AJG - add opt version of ant bin to path
export ANT_HOME="/opt/ant"
export JAVA_7_HOME=/opt/java/java7/
export JAVA_8_HOME=/opt/java/java8/
#export JAVA_HOME=/usr/lib/jvm/default-java
export JAVA_HOME=/opt/java/java
export PATH="$HOME/bin:$JAVA_HOME/bin:$ANT_HOME/bin:$PATH"


function prompts
{
    case $TERM in
        xterm*)
            # local TITLEBAR='\[\033]0;\u@\h:\W\007\]'     WITH user@host
            local TITLEBAR='\[\033]0;\w\007\]'
            local PROMPT='\[\e[0;32m\]\t \[\e[m\] \[\e[1;34m\]\w\[\e[m\] \[\e[1;32m\] [\#] >> \[\e[m\] \[\e[1;37m\]'
            ;;
        *)
            local TITLEBAR=''
            local PROMPT=''
            ;;
    esac

    PS1="${TITLEBAR}\
${PROMPT} "
    PS2='> '
    PS4='+ '
}

# set up prompts - title bar and command line
prompts

# get my aliases 
source ~/.aliases

# If id command returns zero, you’ve root access.
#if [ $(id -u) -eq 0 ];
#then # you are root, set red colour prompt
#  PS1='\[\e[1;34m\][\W]\$\[\e[0m\] '
#else # normal
#  PS1='\[\e[0;32m\]\t \[\e[m\] \[\e[1;34m\]\w\[\e[m\] \[\e[1;32m\] [\#] >> \[\e[m\] \[\e[1;37m\]'
#fi


# mint-fortune
/usr/bin/mint-fortune


