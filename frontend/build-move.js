import fs from 'fs-extra';
import mv from 'mv';

// 원본 디렉터리 위치
const srcDirPath = "dist";

// 대상 디렉터리 위치
const dstDirPath = "../src/main/resources/static";

async function copyDist(){

    try{
        //dist 내용을 static으로 복사(기존 파일 덮어씀, 삭제 안함)
        await fs.copy(srcDirPath, dstDirPath,{overwrite : true});
        console.log("dist 폴더를 static으로 복사 완료 (기존 static 유지)")
    }catch(err){
        console.error("에러발생: ",err);

    }

}

copyDist();

// // 대상 디렉터리 삭제
// fs.rmSync(dstDirPath, {recursive: true, force: true});
//
// // 이동 작업 수행
// mv(srcDirPath, dstDirPath, {mkdirp: true}, function (err) {
//     console.log(err || 'complete!');
// });