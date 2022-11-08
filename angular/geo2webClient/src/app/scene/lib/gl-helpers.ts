//source: https://github.com/ndesmic/geogl/blob/v8/js/lib/gl-helpers.js

export function loadShader(context: any, url: any, type: any){
  return fetch(url)
    .then(r => r.text())
    .then(txt => compileShader(context, txt, type));
}

export async function loadProgram(context: any, url: any){
  const [vertexShader, fragmentShader] = await Promise.all([
    loadShader(context, `${url}.vert.glsl`, context.VERTEX_SHADER),
    loadShader(context, `${url}.frag.glsl`, context.FRAGMENT_SHADER)
  ]);
  return compileProgram(context, vertexShader, fragmentShader);
}

export function compileShader(context: any, text: any, type: any) {
  const shader = context.createShader(type);
  context.shaderSource(shader, text);
  context.compileShader(shader);

  if (!context.getShaderParameter(shader, context.COMPILE_STATUS)) {
    throw new Error(`Failed to compile ${type === context.VERTEX_SHADER ? "vertex" : "fragment"} shader: ${context.getShaderInfoLog(shader)}`);
  }
  return shader;
}

export function compileProgram(context: any, vertexShader: any, fragmentShader: any) {
  const program = context.createProgram();
  context.attachShader(program, vertexShader);
  context.attachShader(program, fragmentShader);
  context.linkProgram(program);

  if (!context.getProgramParameter(program, context.LINK_STATUS)) {
    throw new Error(`Failed to compile WebGL program: ${context.getProgramInfoLog(program)}`);
  }

  return program;
}

export function bindAttribute(context: any, attributes: any, attributeName: any, size: any, program: any){
  program = program ?? context.getParameter(context.CURRENT_PROGRAM);
  attributes = attributes instanceof Float32Array ? attributes : new Float32Array(attributes);

  const attributeLocation = context.getAttribLocation(program, attributeName);
  if(attributeLocation === -1) return; //bail if it doesn't exist in the shader
  const attributeBuffer = context.createBuffer();
  context.bindBuffer(context.ARRAY_BUFFER, attributeBuffer);

  context.bufferData(context.ARRAY_BUFFER, attributes, context.STATIC_DRAW);

  context.enableVertexAttribArray(attributeLocation);
  context.vertexAttribPointer(attributeLocation, size, context.FLOAT, false, 0, 0);
}
