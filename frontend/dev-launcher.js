import { spawn } from 'child_process';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const args = process.argv.slice(2);
const isCli = args.includes('--cli') || args.includes('-cli') || process.env.CLI === 'true';

if (isCli) {
  console.log('Starting AgentMesh in CLI Mode...');
  const cliPath = path.join(__dirname, 'src', 'cli.js');
  const child = spawn('node', [cliPath], { stdio: 'inherit' });
  child.on('exit', (code) => process.exit(code || 0));
} else {
  const viteBin = path.join(__dirname, 'node_modules', '.bin', 'vite');
  const child = spawn(viteBin, args, { stdio: 'inherit', shell: true });
  child.on('exit', (code) => process.exit(code || 0));
}
