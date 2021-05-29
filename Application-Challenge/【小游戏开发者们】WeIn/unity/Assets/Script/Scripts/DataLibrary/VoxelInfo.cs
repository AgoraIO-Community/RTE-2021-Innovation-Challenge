using UnityEngine;
[CreateAssetMenu(fileName = "New Voxel", menuName = "Voxel")]
public class VoxelInfo : ScriptableObject
{
    public Material material;
    public Mesh mesh;
    public Vector3 posOffset = Vector3.zero;
    public Vector3 rotOffset = Vector3.zero;
    public Vector3 scale = Vector3.one;
}
