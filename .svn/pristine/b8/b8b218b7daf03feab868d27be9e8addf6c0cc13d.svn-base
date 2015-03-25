#init environment
source  build/envsetup.sh

function usage() {
cat <<EOF
Invoke ". build/envsetup.sh" from your shell to add the following functions to your environment:
- lunch:   lunch <product_name>-<build_variant>
- tapas:   tapas [<App1> <App2> ...] [arm|x86|mips] [eng|userdebug|user]
- croot:   Changes directory to the top of the tree.
- m:       Makes from the top of the tree.
- mm:      Builds all of the modules in the current directory.
- mmm:     Builds all of the modules in the supplied directories.
- cgrep:   Greps on all local C/C++ files.
- jgrep:   Greps on all local Java files.
- resgrep: Greps on all local res/*.xml files.
- godir:   Go to the directory containing a file.

Look at the source to view more functions. The complete list is:
EOF
    T=$(gettop)
    local A
    A=""
    for i in `cat $T/build/refinedrawable.sh | sed -n "/^function /s/function \([a-z_]*\).*/\1/p" | sort`; do
      A="$A $i"
    done
    echo $A

}

# Get the value of a build variable as an absolute path.
function refine_drawable()
{
   TOP=$(gettop)
   #params='-sw 600 -density tvdpi'
   params=$@
   echo $params
   
   echo $params | grep -q "path"
   result=$?

   if [ -z $params ]; then

	echo "refine all packages : -sw 600 -density tvdpi -exclude xhdpi,xxhdpi"
	echo "refine single package : -sw 600 -density tvdpi -exclude xhdpi,xxhdpi -path packages/apps/AsusDeskClock"

   elif [ $result -eq 0 ];then
	echo "single package :  $params/res" 
        #sed -i '/include $(BUILD_PACKAGE)/ i LOCAL_AAPT_INCLUDE_ALL_RESOURCES := true' $params/Android.mk
	java -jar $TOP/build/RefineDrawable.jar $params/res

   else
	echo "refine all packages..."
	#repos=$(find $TOP/packages/  -name '.git' )
	repos=$(cat $TOP/.repo/project.list | grep  'packages' )

	for i in $repos; do
		param="$params -path $i/res"

		echo $param
		#sed -i '/include $(BUILD_PACKAGE)/ i LOCAL_AAPT_INCLUDE_ALL_RESOURCES := true' $i/Android.mk
		java -jar $TOP/build/RefineDrawable.jar $param
	done

	rm -rf $TOP/out/target/common/obj/APPS
  fi
}


function restore_drawable()
{
  # local TOP
    local repos
    TOP=$(gettop)
    echo $@

    if [ $# -ge 1 ]; then
	cd $TOP/$1
	git reset HEAD --hard
        cd $TOP
	echo "test"
        #mmm $1        
    else
	    #repos=$(find $TOP/packages/  -name '.git' )
	    repos=$(cat $TOP/.repo/project.list | grep  'packages' )
	    for i in $repos; do
		repo="$TOP/$i"
		cd $repo
		echo $repo
	        rm -rf $repo/*.*
		git reset HEAD --hard
	    done
    fi

    cd $TOP

}


function refine_project()
{
  	if [ $1 == "ME175KG" ];then
		refine_drawable -sw 600 -density tvdpi -exclude xhdpi,xxhdpi
		echo "ME175KG refine_drawable -sw 600 -density tvdpi -exclude xhdpi,xxhdpi"
	elif [ $1 == "A68M" ];then
		refine_drawable -sw 800,360 -density mdpi,xhdpi
		echo "A68M refine_drawable -sw 800,360 -density mdpi,xhdpi"
	elif [ $1 == "A11" ];then
		refine_drawable -sw 600,360 -density tvdpi,hdpi -exclude xhdpi,xxhdpi
		echo "A11 refine_drawable -sw 600,360 -density tvdpi,hdpi -exclude xhdpi,xxhdpi"
	fi	
}




# Get the value of a build variable as an absolute path.
function refine_single_apk()
{  
   echo $@
   apk_dir=$1

   TOP=$(gettop)
   amax_prebuilt_path=$TOP/vendor/amax-prebuilt
   packages=$(ls $TOP/vendor/amax-prebuilt)
   apk_path=$amax_prebuilt_path/$apk_dir
   apk_names=$(ls $apk_path | grep "apk")
   out_dir=$apk_path/out
    
   echo "before refine: $(du -s -h $apk_path)" >> $amax_prebuilt_path/refine_result.log
   
   apk_count=0
   for apk_name in $apk_names; do
        apk_count=$(($apk_count + 1))   
        
      
   
        if [ $apk_count != 1 ];then
          echo "more than one or empty apk in " $apk_path >> $amax_prebuilt_path/error.log
        fi	
	#unzip
	#rm -rf $apk_path/out
	#rm $apk_path/out.apk   
	mkdir -p $apk_path/out
	unzip -q $apk_path/$apk_name -d $out_dir
        #refine drawable you can modiry paramater for different project, sample for nexus 7,sw600-tvdpi	
	java -jar $TOP/build/RefineDrawable.jar -sw 360 -density xhdpi -exclude xxhdpi -path $out_dir/res 

	#remove certification files
	rm -rf $out_dir/META-INF
	rm $out_dir/resDelete.log
	   
	#zip
	cd $out_dir
	zip -q -r $apk_path/out.apk *
	   
	#clean
	rm $apk_path/$apk_name
	mv $apk_path/out.apk $apk_path/$apk_name   
	cd $TOP
	rm -rf $out_dir   
   
   done
   
   echo "end refine: $(du -s -h $apk_path)" >> $amax_prebuilt_path/refine_result.log
}



function refine_prebuilt_apks()
{
   TOP=$(gettop)
   repos=$(ls $TOP/vendor/amax-prebuilt)

  echo $repos
	
   for i in $repos; do	
    echo $i
    refine_single_apk $i	
  done
}


function restore_prebuilt_apks()
{
  TOP=$(gettop)
  rm -rf $TOP/vendor/amax-prebuilt/*
  
  cd $TOP/vendor/amax-prebuilt
  git reset --hard
  cd $TOP
}




