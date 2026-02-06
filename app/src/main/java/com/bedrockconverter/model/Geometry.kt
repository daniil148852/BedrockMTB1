// app/src/main/java/com/bedrockconverter/model/Geometry.kt
package com.bedrockconverter.model

/**
 * Represents a generic 3D geometry with meshes and bones
 */
data class Geometry(
    val meshes: List<Mesh> = emptyList(),
    val bones: List<Bone> = emptyList()
) {
    fun combinedMesh(): Mesh {
        if (meshes.isEmpty()) return Mesh()
        if (meshes.size == 1) return meshes[0]

        val vertices = mutableListOf<Float>()
        val indices = mutableListOf<Int>()
        val uvs = mutableListOf<Float>()
        val normals = mutableListOf<Float>()
        var indexOffset = 0

        for (mesh in meshes) {
            vertices.addAll(mesh.vertices.toList())
            uvs.addAll(mesh.uvs.toList())
            normals.addAll(mesh.normals.toList())
            for (index in mesh.indices) {
                indices.add(index + indexOffset)
            }
            indexOffset += mesh.vertices.size / 3
        }

        return Mesh(
            vertices = vertices.toFloatArray(),
            indices = indices.toIntArray(),
            uvs = uvs.toFloatArray(),
            normals = normals.toFloatArray()
        )
    }

    fun calculateBounds(): BoundingBox {
        val combined = combinedMesh()
        return BoundingBox.fromVertices(combined.vertices)
    }

    fun scaled(scale: Float): Geometry {
        return Geometry(
            meshes = meshes.map { mesh ->
                mesh.copy(
                    vertices = mesh.vertices.map { it * scale }.toFloatArray()
                )
            },
            bones = bones.map { bone ->
                bone.copy(
                    pivot = bone.pivot * scale,
                    bindPose = bone.bindPose.copy(
                        position = bone.bindPose.position * scale
                    )
                )
            }
        )
    }
}

/**
 * A single mesh with vertices, indices, and UVs
 */
data class Mesh(
    val vertices: FloatArray = floatArrayOf(),
    val indices: IntArray = intArrayOf(),
    val uvs: FloatArray = floatArrayOf(),
    val normals: FloatArray = floatArrayOf()
) {
    val hasNormals: Boolean get() = normals.isNotEmpty()
    val hasUvs: Boolean get() = uvs.isNotEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Mesh

        if (!vertices.contentEquals(other.vertices)) return false
        if (!indices.contentEquals(other.indices)) return false
        if (!uvs.contentEquals(other.uvs)) return false
        if (!normals.contentEquals(other.normals)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = vertices.contentHashCode()
        result = 31 * result + indices.contentHashCode()
        result = 31 * result + uvs.contentHashCode()
        result = 31 * result + normals.contentHashCode()
        return result
    }
}

/**
 * Generic bone for model hierarchy
 */
data class Bone(
    val name: String,
    val parent: String? = null,
    val pivot: Vector3 = Vector3.ZERO,
    val rotation: Vector3 = Vector3.ZERO,
    val bindPose: Transform = Transform.IDENTITY
)

/**
 * Minecraft Bedrock specific geometry
 */
data class BedrockGeometry(
    val formatVersion: String = "1.12.0",
    val identifier: String,
    val textureWidth: Int = 64,
    val textureHeight: Int = 64,
    val visibleBoundsWidth: Float = 1f,
    val visibleBoundsHeight: Float = 1f,
    val visibleBoundsOffset: Vector3 = Vector3.ZERO,
    val bones: List<BedrockBone> = emptyList()
)

/**
 * Minecraft Bedrock specific bone
 */
data class BedrockBone(
    val name: String,
    val parent: String? = null,
    val pivot: Vector3 = Vector3.ZERO,
    val rotation: Vector3 = Vector3.ZERO,
    val cubes: List<BedrockCube> = emptyList()
)

/**
 * Minecraft Bedrock specific cube
 */
data class BedrockCube(
    val origin: Vector3,
    val size: Vector3,
    val uv: Vector2,
    val inflate: Float? = null,
    val mirror: Boolean? = null,
    val pivot: Vector3? = null,
    val rotation: Vector3? = null
)

/**
 * Internal helper for voxelization
 */
data class VoxelCoord(val x: Int, val y: Int, val z: Int)

/**
 * Internal helper for voxel optimization
 */
data class VoxelCube(val origin: Vector3, val size: Vector3)
