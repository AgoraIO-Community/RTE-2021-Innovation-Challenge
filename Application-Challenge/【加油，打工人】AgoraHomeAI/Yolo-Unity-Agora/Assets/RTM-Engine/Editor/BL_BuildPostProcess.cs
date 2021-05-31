#if UNITY_IPHONE 

using System.IO;
using UnityEditor;
using UnityEditor.Callbacks;
using UnityEditor.iOS.Xcode;
using UnityEditor.iOS.Xcode.Extensions;

namespace agora_rtm {
    public class BL_BuildPostProcess
    {
	    const string defaultLocationInProj = "AgoraEngine/RTM-Engine/Plugins/iOS";

        [PostProcessBuild]
        public static void OnPostprocessBuild(BuildTarget buildTarget, string path)
        {
            if (buildTarget == BuildTarget.iOS)
            {
                LinkLibraries(path);
            }
        }

        static string GetTargetGuid(PBXProject proj)
        {
#if UNITY_2019_3_OR_NEWER
            return proj.GetUnityFrameworkTargetGuid();
#else
            return proj.TargetGuidByName("Unity-iPhone");
#endif
        }

        // The followings are the addtional frameworks to add to the project
        static string[] ProjectFrameworks = new string[] {
            "CoreTelephony.framework",
            "libresolv.9.dylib"
	    };

        static string[] EmbeddedFrameworks = new string[] {
	        "AgoraRtmKit.framework"
	    };

        static void EmbedFramework(PBXProject proj, string target, string frameworkPath)
        {
            string RTMFrameWorkPath = Path.Combine(defaultLocationInProj, frameworkPath);
            string fileGuid = proj.AddFile(RTMFrameWorkPath, "Frameworks/" + RTMFrameWorkPath, PBXSourceTree.Sdk);
            PBXProjectExtensions.AddFileToEmbedFrameworks(proj, target, fileGuid);
        }

        static void LinkLibraries(string path)
        {
            // linked library
            string projPath = path + "/Unity-iPhone.xcodeproj/project.pbxproj";
            PBXProject proj = new PBXProject();
            proj.ReadFromFile(projPath);
            string target = GetTargetGuid(proj);

            // disable bit-code
            proj.SetBuildProperty(target, "ENABLE_BITCODE", "false");

            // Frameworks
            foreach (string framework in ProjectFrameworks)
            {
                proj.AddFrameworkToProject(target, framework, true);
            }

            // embedded frameworks
#if UNITY_2019_1_OR_NEWER
	    target = proj.GetUnityMainTargetGuid();
#endif
	    foreach (string framework in EmbeddedFrameworks) 
	    {
		EmbedFramework(proj, target, framework);
	    }

            proj.SetBuildProperty(target, "LD_RUNPATH_SEARCH_PATHS", "$(inherited) @executable_path/Frameworks");

            // done, write to the project file
            File.WriteAllText(projPath, proj.WriteToString());
        }

    }

}
#endif
