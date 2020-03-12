"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
"++  .vimrc                       15/09/99 Franz Brandl  ++
"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
" see the file /usr/local/share/vim/vimrc for the system
" wide settings. I do add only a few personal things here.
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

" Vim
" An example for a vimrc file.
"
" To use it, copy it to
"     for Unix and OS/2:  ~/.vimrc
"             for Amiga:  s:.vimrc
"  for MS-DOS and Win32:  $VIM\_vimrc

set nocompatible        " Use Vim defaults (much better!)
set bs=2                " allow backspacing over everything in insert mode
set ai                  " always set autoindenting on
set backup              " keep a backup file
set backupdir=$HOME/.vim/vimfiles/backup " where to put backup file
set directory=$HOME/.vim/vimfiles/temp " directory is the directory for temp file
set makeef=error.err " When using make, where should it dump the file
set viminfo='20,\"50    " read/write a .viminfo file, don't store more
                        " than 50 lines of registers
set ignorecase          " search is NOT case sensitive
set incsearch           " incremental search = on
set ruler               " ruler shows where the cursor is 
set number              " line numbers on
set nowrapscan          " by default, search wraps around file end, with this set it WONT
"set textwidth=80        " wrap after 80 chars please
syntax on               " context sensitive colors e.g. java, xml, etc
"set so=999              " setting scrolloff (or shortened 'so') to 999, means that the cursor will
                        " always be in the centre of the screen.
set so=9

set tabstop=3
set shiftwidth=3
set expandtab

" save position & size
set sessionoptions+=resize,winpos

" In text files, always limit the width of text to 78 characters
"autocmd BufRead *.txt set tw=78 

" For Win32 GUI: remove 't' flag from 'guioptions': no tearoff menu entries
" let & guioptions = substitute(& guioptions, "t", "", "g")

" Don't use Ex mode, use Q for formatting
map Q gq

"""""""""""""""""""""""""""""""""""""""""""""
""""""""""" My Stuff
"""""""""""""""""""""""""""""""""""""""""""""
map W       :w
map E       :wq
map Q       :q
map T       :sp ~/.vim_tips
" useful for tidying up dependencies lists in build.xml files
map <F11>   /<dependency name=\"<Home>dwi        <End>
map    /<dependency name=\"<Home>dwi        <End>
" used to get rid of version file updates in an svn log file
map <F12>   /^Updated version fileV?^-----------------d

""""" when i'm editting build.xml files for the clean up dependency task
map       <Home>/<dependency name=\"i<!-- <End> --><Home>W
map       <Home>/<dependency name=\"i<!-- <End> --><Home>W
map       :s/<!-- //:s/ -->//<Home>W
map       <Home>/<dependency name=\"/\/>i destdir="${test.lib.dir}"<Home>W
"map       :s/ destdir="${test.lib.dir}"//<Home>W
map      /licenseIDs\"V/]J0/[<Right>v/]<Left>d:w
""
""
""
"" These macros are used against the bugzilla csv output files to format in the right way
""
map <F5>    :%s/^/\|  /g
map <F6>    :%s/$/  \|/g
map <F7>    :%s/,/  \|  /g
map <F8>    :%s/\"//g
map <F9>   :%s/bugzillaassigner@deveng1.eu.ubiquity.net/UNASSIGNED/g
map <F10>   0wv<Right><Right><Right><Right>yi[[https://portal.ubiquitysoftware.com/bugzilla/show_bug.cgi?id=<Right><Right><Right><Right><Right><Right>i][p<Right>i]]<Down>0
map <F4>    <F5><F6><F7><F8><F9>gg<Down>0
"""""""
if has("gui_running")
  " GUI is running or is about to start.
  " Maximize gvim window (for an alternative on Windows, see simalt below).
  set lines=40 columns=160
else
  " This is console Vim.
  if exists("+lines")
    set lines=50
  endif
  if exists("+columns")
    set columns=100
  endif
endif

"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
""" END OF .vimrc """""""""""""""""""""""""""""""""""""""""
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
