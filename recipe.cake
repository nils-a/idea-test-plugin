#load nuget:https://www.myget.org/F/nils-a/api/v3/index.json?package=Cake.IntelliJ.Recipe&version=0.2.0-update-cake-3-0001&prerelease 

Environment.SetVariableNames();

IntelliJBuildParameters.SetParameters(
  context: Context,
  buildSystem: BuildSystem,
  masterBranchName: "main",
  sourceDirectoryPath: "./src",
  title: "idea-test-plugin",
  repositoryName: "idea-test-plugin",
  repositoryOwner: "nils-a",
  marketplaceId: "15698-test-rider",
  pluginReleaseChannel: "Development",
  preferredBuildProviderType: BuildProviderType.GitHubActions,
  preferredBuildAgentOperatingSystem: PlatformFamily.Linux
);

IntelliJBuildParameters.PrintParameters(Context);

ToolSettings.SetToolSettings(context: Context);

IntelliJBuild.Run();
